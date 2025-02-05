import {MessageEntity} from "@/model/entity/MessageEntity";
import { languageOrder } from "@/model/enums/Language";

export function messagesContent(messages: MessageEntity[]) {
  return messages.sort((a, b) => languageOrder(a.lang) - languageOrder(b.lang))
    .map((message) => `${message.content}(${message.lang})`)
    .join(" | ");
}
