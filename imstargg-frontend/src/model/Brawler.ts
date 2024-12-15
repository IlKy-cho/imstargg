import {BrawlerRarity} from "@/model/BrawlerRarity";
import {BrawlerRole} from "@/model/BrawlerRole";
import Gadget from "@/model/Gadget";
import Gear from "@/model/Gear";
import StarPower from "@/model/StarPower";

export default interface Brawler {
  id: number;
  name: string;
  rarity: BrawlerRarity;
  role: BrawlerRole;
  gadgets: Gadget[];
  gears: Gear[];
  starPowers: StarPower[];
}