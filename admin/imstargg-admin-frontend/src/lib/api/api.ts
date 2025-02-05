export const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export class ApiError extends Error {
  constructor(message: string) {
    super(message);
  }

  static async create(response: Response) {
    return new ApiError(`Failed to fetch from ${response.url}.\n- status: ${response.status}\nbody: ${await response.json()}`);
  }
}

export interface ListResponse<T> {
  content: T[];
}


export async function fetchGetEvents(): Promise<Response> {
  const url = new URL(`${API_BASE_URL}/admin/api/events`);
  console.log(`Fetch from ${url}`);
  return await fetch(url);
}

export async function fetchUploadMapImage(eventId: number, image: File): Promise<Response> {
  const url = new URL(`${API_BASE_URL}/admin/api/battle-events/${eventId}/image`);
  console.log(`PUT to ${url}`);

  const formData = new FormData();
  formData.append('image', image);

  return await fetch(url, {
    method: 'PUT',
    body: formData
  });
}