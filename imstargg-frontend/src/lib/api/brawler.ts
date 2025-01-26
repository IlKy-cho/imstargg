import {Brawler} from "@/model/Brawler";
import {ApiError, BASE_URL, CacheOptions, ListResponse} from "@/lib/api/api";
import { BrawlerRole } from "@/model/enums/BrawlerRole";
import { BrawlerRarity } from "@/model/enums/BrawlerRarity";
import { Gadget } from "@/model/Gadget";
import { Gear } from "@/model/Gear";
import { StarPower } from "@/model/StarPower";

interface BrawlerResponse {
  id: number;
  name: string;
  rarity: BrawlerRarity;
  role: BrawlerRole;
  gadgets: Gadget[];
  gears: Gear[];
  starPowers: StarPower[];
  imagePath: string | null;
}

export async function fetchGetBrawlers(options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/brawlstars/brawlers`);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['brawlers'],
      revalidate: options.revalidate
    }
  });
}

export async function getBrawlers() : Promise<Brawler[]> {
  const response = await fetchGetBrawlers({revalidate: 60 * 60});

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerResponse>;
    return data.content
      .map(brawler => ({
        ...brawler,
        imageUrl: brawler.imagePath ? new URL(brawler.imagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
      }));
  }

  throw await ApiError.create(response);
}

export async function fetchGetBrawler(id: number, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/brawlstars/brawlers/${id}`);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['brawlers', id.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBrawler(id: number) : Promise<Brawler | null> {
  const response = await fetchGetBrawler(id, {revalidate: 60 * 60});

  if (response.ok) {
    const data = await response.json() as BrawlerResponse;
    return {
      ...data,
      imageUrl: data.imagePath ? new URL(data.imagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
    };
  } else if (response.status === 404) {
    return null;
  }

  throw await ApiError.create(response);
}
