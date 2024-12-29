import MessageEntity from "@/model/entity/MessageEntity";

export const messagesToTitle = (messages: MessageEntity[]): string => {
  return messages
    .sort((a, b) => a.lang.localeCompare(b.lang))
    .map(message => `${message.content}(${message.lang})`)
    .join(' ')
}