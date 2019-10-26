/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PostsComponentsPage, PostsDeleteDialog, PostsUpdatePage } from './posts.page-object';

const expect = chai.expect;

describe('Posts e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let postsUpdatePage: PostsUpdatePage;
  let postsComponentsPage: PostsComponentsPage;
  let postsDeleteDialog: PostsDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Posts', async () => {
    await navBarPage.goToEntity('posts');
    postsComponentsPage = new PostsComponentsPage();
    await browser.wait(ec.visibilityOf(postsComponentsPage.title), 5000);
    expect(await postsComponentsPage.getTitle()).to.eq('Posts');
  });

  it('should load create Posts page', async () => {
    await postsComponentsPage.clickOnCreateButton();
    postsUpdatePage = new PostsUpdatePage();
    expect(await postsUpdatePage.getPageTitle()).to.eq('Create or edit a Posts');
    await postsUpdatePage.cancel();
  });

  it('should create and save Posts', async () => {
    const nbButtonsBeforeCreate = await postsComponentsPage.countDeleteButtons();

    await postsComponentsPage.clickOnCreateButton();
    await promise.all([]);
    await postsUpdatePage.save();
    expect(await postsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await postsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Posts', async () => {
    const nbButtonsBeforeDelete = await postsComponentsPage.countDeleteButtons();
    await postsComponentsPage.clickOnLastDeleteButton();

    postsDeleteDialog = new PostsDeleteDialog();
    expect(await postsDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Posts?');
    await postsDeleteDialog.clickOnConfirmButton();

    expect(await postsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
