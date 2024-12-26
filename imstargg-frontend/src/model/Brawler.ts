import {Gadget} from "@/model/Gadget";
import {Gear} from "@/model/Gear";
import {StarPower} from "@/model/StarPower";
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
  imageUrl: string | null;
}

export interface Brawlers {
  find(id: number): Brawler | null;
  all(): Brawler[];
}

export class BrawlersImpl implements Brawlers {
  private readonly brawlers: Record<number, Brawler>;

  constructor(brawlers: Brawler[]) {
    this.brawlers = Object.fromEntries(
      brawlers.map(brawler => [brawler.id, brawler])
    );
  }

  find(id: number | null): Brawler | null {
    if (id === null) {
      return null;
    }
    return this.brawlers[id] || null;
  }

  all(): Brawler[] {
    return Object.values(this.brawlers);
  }
}
