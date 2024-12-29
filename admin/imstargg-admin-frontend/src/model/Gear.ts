import MessageEntity from "@/model/entity/MessageEntity";
import GearEntity from "@/model/entity/GearEntity";

export default interface Gear {
  entity: GearEntity;
  names: MessageEntity[];
}