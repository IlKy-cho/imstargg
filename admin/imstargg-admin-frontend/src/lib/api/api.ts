const BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export class ApiError extends Error {
  constructor(public response: Response, message = `Failed to fetch from ${response.url}`) {
    super(message);
  }

  log() {
    console.log(`Failed to fetch from ${this.response.url}. status: ${this.response.status}, body: ${this.response.body} ex: ${this.message}`);
  }
}


export async function fetchGetEvents(): Promise<Response> {
  const url = new URL(`${BASE_URL}/admin/api/events`);
  console.log(`Fetch from ${url}`);
  return await fetch(url);
}

export async function fetchUploadMapImage(eventId: number, image: File): Promise<Response> {
  const url = new URL(`${BASE_URL}/admin/api/battle-events/${eventId}/image`);
  console.log(`PUT to ${url}`);

  const formData = new FormData();
  formData.append('image', image);

  return await fetch(url, {
    method: 'PUT',
    body: formData
  });
}