import MessageEntity from "@/model/entity/MessageEntity";
import StarPowerEntity from "@/model/entity/StarPowerEntity";
import GearEntity from "@/model/entity/GearEntity";

export default interface Gear {
  entity: GearEntity;
  names: MessageEntity[];
}