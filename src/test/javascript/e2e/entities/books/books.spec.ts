/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BooksComponentsPage, BooksDeleteDialog, BooksUpdatePage } from './books.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Books e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let booksUpdatePage: BooksUpdatePage;
  let booksComponentsPage: BooksComponentsPage;
  let booksDeleteDialog: BooksDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Books', async () => {
    await navBarPage.goToEntity('books');
    booksComponentsPage = new BooksComponentsPage();
    await browser.wait(ec.visibilityOf(booksComponentsPage.title), 5000);
    expect(await booksComponentsPage.getTitle()).to.eq('Books');
  });

  it('should load create Books page', async () => {
    await booksComponentsPage.clickOnCreateButton();
    booksUpdatePage = new BooksUpdatePage();
    expect(await booksUpdatePage.getPageTitle()).to.eq('Create or edit a Books');
    await booksUpdatePage.cancel();
  });

  it('should create and save Books', async () => {
    const nbButtonsBeforeCreate = await booksComponentsPage.countDeleteButtons();

    await booksComponentsPage.clickOnCreateButton();
    await promise.all([
      booksUpdatePage.setTitleInput('title'),
      booksUpdatePage.setContentsInput('contents'),
      booksUpdatePage.setCreatetimeInput('2000-12-31'),
      booksUpdatePage.setImageInput(absolutePath)
    ]);
    expect(await booksUpdatePage.getTitleInput()).to.eq('title', 'Expected Title value to be equals to title');
    expect(await booksUpdatePage.getContentsInput()).to.eq('contents', 'Expected Contents value to be equals to contents');
    expect(await booksUpdatePage.getCreatetimeInput()).to.eq('2000-12-31', 'Expected createtime value to be equals to 2000-12-31');
    expect(await booksUpdatePage.getImageInput()).to.endsWith(fileNameToUpload, 'Expected Image value to be end with ' + fileNameToUpload);
    await booksUpdatePage.save();
    expect(await booksUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await booksComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Books', async () => {
    const nbButtonsBeforeDelete = await booksComponentsPage.countDeleteButtons();
    await booksComponentsPage.clickOnLastDeleteButton();

    booksDeleteDialog = new BooksDeleteDialog();
    expect(await booksDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Books?');
    await booksDeleteDialog.clickOnConfirmButton();

    expect(await booksComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
