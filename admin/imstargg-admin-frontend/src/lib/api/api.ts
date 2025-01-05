const BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export async function fetchGetEvents(): Promise<Response> {
  const url = new URL(`${BASE_URL}/admin/api/events`);
  console.log(`Fetch from ${url}`);
  return await fetch(url);
}

export async function fetchEventSeasoned(eventId: number): Promise<Response> {
  const url = new URL(`${BASE_URL}/admin/api/events/${eventId}/season`);
  console.log(`Fetch from ${url}`);
  return await fetch(url,  {
    method: 'PUT'
  });
}

export async function fetchEventUnseasoned(eventId: number): Promise<Response> {
  const url = new URL(`${BASE_URL}/admin/api/events/${eventId}/season`);
  console.log(`Fetch from ${url}`);
  return await fetch(url,  {
    method: 'DELETE'
  });
}

export async function fetchDeleteEvent(eventId: number): Promise<Response> {
  const url = new URL(`${BASE_URL}/admin/api/events/${eventId}`);
  console.log(`Fetch from ${url}`);
  return await fetch(url,  {
    method: 'DELETE'
  });
}

export async function fetchRestoreEvent(eventId: number): Promise<Response> {
  const url = new URL(`${BASE_URL}/admin/api/events/${eventId}/restore`);
  console.log(`Fetch from ${url}`);
  return await fetch(url,  {
    method: 'POST'
  });
}

export async function fetchGetMapList(): Promise<Response> {
  const url = new URL(`${BASE_URL}/admin/api/maps`);
  console.log(`Fetch from ${url}`);
  return await fetch(url);
}

export async function fetchGetNotRegisteredEventList(): Promise<Response> {
  const url = new URL(`${BASE_URL}/admin/api/not-registered-events`);
  console.log(`Fetch from ${url}`);
  return await fetch(url);
}