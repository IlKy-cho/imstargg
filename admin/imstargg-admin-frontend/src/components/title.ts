import MessageEntity from "@/model/entity/MessageEntity";

export const messagesToTitle = (messages: MessageEntity[]): string => {
  if (!messages) {
    return '';
  }
  return messages
    .sort((a, b) => a.lang.localeCompare(b.lang))
    .map(message => `${message.content}(${message.lang})`)
    .join(' ')
}