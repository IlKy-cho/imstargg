const BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

interface CacheOptions {
  revalidate?: number | false;
}

export async function fetchSearchPlayer(query: string, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/player/search`);
  url.searchParams.append('query', query);
  console.log(`Fetch from ${url}`);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['player', 'search', query],
      revalidate: options.revalidate,
    }
  });
}

export async function fetchGetRenewalStatus(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/renewal-status`);
  console.log(`Fetch from ${url}`);
  return await fetch(url, {
    next: {
      tags: ['players', tag, 'renewal-status']
    }
  });
}

export async function fetchRenewPlayer(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/renew`);
  console.log(`Fetch from ${url}`);

  return await fetch(url, {
    method: 'POST',
  });
}

export async function fetchGetPlayer(tag: string, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}`);
  console.log(`Fetch from ${url}`);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['players', tag],
      revalidate: options.revalidate
    }
  });
}

export async function fetchGetBattles(tag: string, page: number = 1, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/battles`);
  url.searchParams.append('page', page.toString());
  console.log(`Fetch from ${url}`);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['players', tag, 'battles', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function fetchGetBrawlers(options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/brawlers`);
  console.log(`Fetch from ${url}`);
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