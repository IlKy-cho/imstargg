import {BrawlerEntity} from "@/model/entity/BrawlerEntity";
import {MessageEntity} from "@/model/entity/MessageEntity";
import {BrawlStarsImageEntity} from "@/model/entity/BrawlStarsImageEntity";
import {Gadget} from "@/model/Gadget";
import {StarPower} from "@/model/StarPower";
import {Gear} from "@/model/Gear";

export interface Brawler {
  entity: BrawlerEntity;
  names: MessageEntity[];
  image: BrawlStarsImageEntity | null;
  gadgets: Gadget[];
  starPowers: StarPower[];
  gears: Gear[];
}