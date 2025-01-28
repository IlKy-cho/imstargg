import { Country } from "@/model/enums/Country";
import { ApiError, BASE_URL, CacheOptions, ListResponse } from "./api";
import { PlayerRanking } from "@/model/PlayerRanking";

export async function fetchGetPlayerRanking(country: Country, options?: CacheOptions) {
    const url = new URL(`${BASE_URL}/api/v1/rankings/${country}/players`);
    if (!options) {
        return await fetch(url);
    }

    return await fetch(url, {
        next: {
            tags: ['rankings', country, 'players'],
            revalidate: options.revalidate
        }
    });
}

export async function getPlayerRanking(country: Country): Promise<PlayerRanking[]> {
    const response = await fetchGetPlayerRanking(country, {revalidate: 5 * 60});
    if (response.ok) {
        const data = await response.json() as ListResponse<PlayerRanking>;
        return data.content;
    }

    throw await ApiError.create(response);
}

export async function fetchGetBrawlerRanking(country: Country, brawlerId: number, options?: CacheOptions) {
    const url = new URL(`${BASE_URL}/api/v1/rankings/${country}/brawlers/${brawlerId}`);
    if (!options) {
        return await fetch(url);
    }

    return await fetch(url, {
        next: {
            tags: ['rankings', country, 'brawlers', brawlerId.toString()],
            revalidate: options.revalidate
        }
    });
}

export async function getBrawlerRanking(country: Country, brawlerId: number): Promise<PlayerRanking[]> {
    const response = await fetchGetBrawlerRanking(country, brawlerId, {revalidate: 5 * 60});
    if (response.ok) {
        const data = await response.json() as ListResponse<PlayerRanking>;
        return data.content;
    }

    throw await ApiError.create(response);
}
