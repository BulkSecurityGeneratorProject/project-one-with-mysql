import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class BooksComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-books div table .btn-danger'));
  title = element.all(by.css('jhi-books div h2#page-heading span')).first();

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

export class BooksUpdatePage {
  pageTitle = element(by.id('jhi-books-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  titleInput = element(by.id('field_title'));
  contentsInput = element(by.id('field_contents'));
  createtimeInput = element(by.id('field_createtime'));
  imageInput = element(by.id('file_image'));

  async getPageTitle() {
    return this.pageTitle.getText();
  }

  async setTitleInput(title) {
    await this.titleInput.sendKeys(title);
  }

  async getTitleInput() {
    return await this.titleInput.getAttribute('value');
  }

  async setContentsInput(contents) {
    await this.contentsInput.sendKeys(contents);
  }

  async getContentsInput() {
    return await this.contentsInput.getAttribute('value');
  }

  async setCreatetimeInput(createtime) {
    await this.createtimeInput.sendKeys(createtime);
  }

  async getCreatetimeInput() {
    return await this.createtimeInput.getAttribute('value');
  }

  async setImageInput(image) {
    await this.imageInput.sendKeys(image);
  }

  async getImageInput() {
    return await this.imageInput.getAttribute('value');
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

export class BooksDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-books-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-books'));

  async getDialogTitle() {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
