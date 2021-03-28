import { IType } from 'app/entities/type/type.model';

export interface IZikr {
  id?: number;
  content?: string | null;
  count?: number | null;
  title?: IType | null;
}

export class Zikr implements IZikr {
  constructor(public id?: number, public content?: string | null, public count?: number | null, public title?: IType | null) {}
}

export function getZikrIdentifier(zikr: IZikr): number | undefined {
  return zikr.id;
}
