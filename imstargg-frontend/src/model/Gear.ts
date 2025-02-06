import {GearRarity} from "@/model/enums/GearRarity";

export interface Gear {
  id: number;
  name: string;
  rarity: GearRarity;
  imageUrl: string | null;
}