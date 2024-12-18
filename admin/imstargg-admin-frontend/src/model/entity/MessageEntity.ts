import BaseEntity from "@/model/entity/BaseEntity";
import {Language} from "@/model/enums/Language";

export default interface MessageEntity extends BaseEntity {
  id: number;
  code: string;
  lang: Language;
  content: string;
}