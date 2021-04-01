import { IZikr } from 'app/entities/zikr/zikr.model';

export interface IType {
  id?: number;
  title?: string | null;
  color?: string | null;
  img?: string | null;
  zikrs?: IZikr[] | null;
}

export class Type implements IType {
  constructor(
    public id?: number,
    public title?: string | null,
    public color?: string | null,
    public img?: string | null,
    public zikrs?: IZikr[] | null
  ) {}
}

export function getTypeIdentifier(type: IType): number | undefined {
  return type.id;
}
