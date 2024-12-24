const BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export async function fetchSearchPlayer(query: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/player/search`);
  url.searchParams.append('query', query);
  console.log(`Fetch from ${url}`);
  return await fetch(url, {
    next: {
      tags: ['player', 'search', query]
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

export async function fetchGetPlayer(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}`);
  console.log(`Fetch from ${url}`);
  return await fetch(url, {
    next: {
      tags: ['players', tag]
    }
  });
}