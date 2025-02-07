import {Brawler} from "@/model/Brawler";
import {ApiError, BASE_URL, CacheOptions, ListResponse} from "@/lib/api/api";
import {BrawlerRole} from "@/model/enums/BrawlerRole";
import {BrawlerRarity} from "@/model/enums/BrawlerRarity";
import {GearRarity} from "@/model/enums/GearRarity";

interface BrawlerResponse {
  id: number;
  name: string;
  rarity: BrawlerRarity;
  role: BrawlerRole;
  gadgets: GadgetResponse[];
  gears: GearResponse[];
  starPowers: StarPowerResponse[];
  imagePath: string | null;
}

interface GadgetResponse {
  id: number;
  name: string;
  imagePath: string | null;
}

interface GearResponse {
  id: number;
  name: string;
  rarity: GearRarity;
  imagePath: string | null;
}

interface StarPowerResponse {
  id: number;
  name: string;
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
        gadgets: brawler.gadgets.map(gadget => ({
          ...gadget,
          imageUrl: gadget.imagePath ? new URL(gadget.imagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
        })),
        gears: brawler.gears.map(gear => ({
          ...gear,
          imageUrl: gear.imagePath ? new URL(gear.imagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
        })),
        starPowers: brawler.starPowers.map(starPower => ({
          ...starPower,
          imageUrl: starPower.imagePath ? new URL(starPower.imagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
        })),
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
      gadgets: data.gadgets.map(gadget => ({
        ...gadget,
        imageUrl: gadget.imagePath ? new URL(gadget.imagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
      })),
      gears: data.gears.map(gear => ({
        ...gear,
        imageUrl: gear.imagePath ? new URL(gear.imagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
      })),
      starPowers: data.starPowers.map(starPower => ({
        ...starPower,
        imageUrl: starPower.imagePath ? new URL(starPower.imagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
      })),
      imageUrl: data.imagePath ? new URL(data.imagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
    };
  } else if (response.status === 404) {
    return null;
  }

  throw await ApiError.create(response);
}
