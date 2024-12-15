import {GearRarity} from "@/model/GearRarity";

export default interface Gear {
  id: number;
  name: string;
  rarity: GearRarity;
}