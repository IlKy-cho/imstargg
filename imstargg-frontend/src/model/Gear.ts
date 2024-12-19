import {GearRarityType} from "@/model/enums/GearRarity";

export default interface Gear {
  id: number;
  name: string;
  rarity: GearRarityType;
}