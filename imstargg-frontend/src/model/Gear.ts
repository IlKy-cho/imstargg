import {GearRarityType} from "@/model/enums/GearRarity";

export interface Gear {
  id: number;
  name: string;
  rarity: GearRarityType;
}