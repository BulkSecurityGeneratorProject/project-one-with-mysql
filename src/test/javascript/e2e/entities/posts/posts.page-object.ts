import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class PostsComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-posts div table .btn-danger'));
  title = element.all(by.css('jhi-posts div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getText();
  }
}

export class PostsUpdatePage {
  pageTitle = element(by.id('jhi-posts-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  async getPageTitle() {
    return this.pageTitle.getText();
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class PostsDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-posts-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-posts'));

  async getDialogTitle() {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
