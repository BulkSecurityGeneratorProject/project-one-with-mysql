export interface IPosts {
  id?: number;
}

export class Posts implements IPosts {
  constructor(public id?: number) {}
}
