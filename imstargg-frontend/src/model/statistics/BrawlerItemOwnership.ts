import {ItemRate} from "@/model/ItemRate";

export interface BrawlerItemOwnership {
  starPowers: ItemRate[];
  gadgets: ItemRate[];
  gears: ItemRate[];
}

export const findBrawlerStarPowerOwnershipRate
  = (brawlerOwnershipRate: BrawlerItemOwnership, starPowerId: number): number | null =>
  brawlerOwnershipRate.starPowers.find(starPower => starPower.id === starPowerId)?.value ?? null;

export const findBrawlerGadgetOwnershipRate
  = (brawlerOwnershipRate: BrawlerItemOwnership, gadgetId: number): number | null =>
  brawlerOwnershipRate.gadgets.find(g => g.id === gadgetId)?.value ?? null;

export const findBrawlerGearOwnershipRate
  = (brawlerOwnershipRate: BrawlerItemOwnership, gearId: number): number | null =>
  brawlerOwnershipRate.gears.find(g => g.id === gearId)?.value ?? null;
