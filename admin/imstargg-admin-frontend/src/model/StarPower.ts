import MessageEntity from "@/model/entity/MessageEntity";
import StarPowerEntity from "@/model/entity/StarPowerEntity";

export default interface StarPower {
  entity: StarPowerEntity;
  names: MessageEntity[];
}