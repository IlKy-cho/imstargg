import {Gadget} from "@/model/Gadget";
import {Gear} from "@/model/Gear";
import {StarPower} from "@/model/StarPower";
import {BrawlerRarity} from "@/model/enums/BrawlerRarity";
import {BrawlerRole} from "@/model/enums/BrawlerRole";

export interface Brawler {
  id: number;
  name: string;
  rarity: BrawlerRarity;
  role: BrawlerRole;
  gadgets: Gadget[];
  gears: Gear[];
  starPowers: StarPower[];
  imageUrl: string | null;
}

export class BrawlerCollection {
  private readonly brawlers: Record<number, Brawler>;

  constructor(brawlers: Brawler[]) {
    this.brawlers = Object.fromEntries(
      brawlers
        .map(brawler => [brawler.id, brawler])
    );
  }

  find(id: number | null): Brawler | null {
    if (id === null) {
      return null;
    }
    return this.brawlers[id] || null;
  }

  all(): Brawler[] {
    return Object.values(this.brawlers)
      .sort((a, b) => a.id - b.id);
  }

  allSortedByName(): Brawler[] {
    return Object.values(this.brawlers)
      .sort((a, b) => a.name.localeCompare(b.name));
  }

  allByRole(): Record<BrawlerRole, Brawler[]> {
    return this.all().reduce((acc, brawler) => {
      if (!acc[brawler.role]) {
        acc[brawler.role] = [];
      }
      acc[brawler.role].push(brawler);
      return acc;
    }, {} as Record<BrawlerRole, Brawler[]>);
  }

  allByRarity(): Record<BrawlerRarity, Brawler[]> {
    return this.all().reduce((acc, brawler) => {
      if (!acc[brawler.rarity]) {
        acc[brawler.rarity] = [];
      }
      acc[brawler.rarity].push(brawler);
      return acc;
    }, {} as Record<BrawlerRarity, Brawler[]>);
  }
}
