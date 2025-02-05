import {BaseEntity} from "@/model/entity/BaseEntity";
import {Language} from "@/model/enums/Language";

export interface MessageEntity extends BaseEntity {
  id: number;
  code: string;
  lang: Language;
  content: string;
}