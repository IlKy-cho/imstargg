export const ApiErrorTypeValue = {
    PLAYER_NOT_FOUND : 'PLAYER_NOT_FOUND',
    PLAYER_ALREADY_RENEWED : 'PLAYER_ALREADY_RENEWED',
    PLAYER_RENEW_UNAVAILABLE : 'PLAYER_RENEW_UNAVAILABLE',
} as const;

export type ApiErrorType = typeof ApiErrorTypeValue[keyof typeof ApiErrorTypeValue];

export interface ErrorResponse {
    type: ApiErrorType;
    message: string;
}

export interface ProblemDetail {
    type: URL;
    title: string | null;
    status: number;
    detail: string | null;
    instance: URL | null;
    properties: Map<string, object> | null;
}

export interface ApiResponse<T> {
    ok: boolean;
    status: number;
    data?: T;
    error?: ErrorResponse;
    problemDetail?: ProblemDetail;
}

function isApiErrorType(type: unknown): type is ApiErrorType {
    return typeof type === 'string' && (type as ApiErrorType) in ApiErrorTypeValue;
}

export async function createApiResponse<T>(response: Response): Promise<ApiResponse<T>> {
    const json = await response.json();
    if (response.ok) {
        return {
            ok: response.ok,
            status: response.status,
            data: json as T,
        };
    }
    
    if (json && isApiErrorType(json.type)) {
        return {
            ok: false,
            status: response.status,
            error: json as ErrorResponse,
        };
    }
    
    const { type, title, status, detail, instance, ...remainingProperties } = json;
    
    return {
        ok: false,
        status: response.status,
        problemDetail: {
            type: new URL(type),
            title,
            status,
            detail,
            instance: instance ? new URL(instance) : null,
            properties: remainingProperties
        }
    };
}