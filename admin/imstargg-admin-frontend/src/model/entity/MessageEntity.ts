import BaseEntity from "@/model/entity/BaseEntity";
import {LanguageType} from "@/model/enums/Language";

export default interface MessageEntity extends BaseEntity {
  id: number;
  code: string;
  lang: LanguageType;
  content: string;
}