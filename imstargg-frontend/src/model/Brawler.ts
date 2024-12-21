import Gadget from "@/model/Gadget";
import Gear from "@/model/Gear";
import StarPower from "@/model/StarPower";
import {BrawlerRarityType} from "@/model/enums/BrawlerRarity";
import {BrawlerRoleType} from "@/model/enums/BrawlerRole";

export interface Brawler {
  id: number;
  name: string;
  rarity: BrawlerRarityType;
  role: BrawlerRoleType;
  gadgets: Gadget[];
  gears: Gear[];
  starPowers: StarPower[];
}