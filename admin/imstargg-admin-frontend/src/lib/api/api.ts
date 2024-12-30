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
