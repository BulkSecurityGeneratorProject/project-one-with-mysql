import { Moment } from 'moment';

export interface IBoard {
  id?: number;
  title?: string;
  contents?: string;
  createtime?: Moment;
  imageContentType?: string;
  image?: any;
}

export class Board implements IBoard {
  constructor(
    public id?: number,
    public title?: string,
    public contents?: string,
    public createtime?: Moment,
    public imageContentType?: string,
    public image?: any
  ) {}
}
