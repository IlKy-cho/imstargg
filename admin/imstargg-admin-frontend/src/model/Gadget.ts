import GadgetEntity from "@/model/entity/GadgetEntity";
import MessageEntity from "@/model/entity/MessageEntity";

export default interface Gadget {
  entity: GadgetEntity;
  names: MessageEntity[];
}