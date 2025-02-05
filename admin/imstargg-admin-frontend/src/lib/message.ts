import {MessageEntity} from "@/model/entity/MessageEntity";
import {Language, languageOrder, LanguageValues} from "@/model/enums/Language";

export function messagesContent(messages: MessageEntity[]) {
  return messages.sort((a, b) => languageOrder(a.lang) - languageOrder(b.lang))
    .map((message) => `${message.content}(${message.lang})`)
    .join(" | ");
}

export const initialNewMessages: Record<Language, string> =
  Object.fromEntries(LanguageValues.map(lang => [lang, ""])) as Record<Language, string>;