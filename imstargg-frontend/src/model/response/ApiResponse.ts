import {ApiErrorType, ApiErrorTypeType} from "@/model/response/ApiErrorType";

export interface ErrorResponse {
    type: ApiErrorTypeType;
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
    data: T | null;
    error: ErrorResponse | null;
    problemDetail: ProblemDetail | null;
}

function isApiErrorType(type: unknown): type is ApiErrorTypeType {
    return typeof type === 'string' && (type as ApiErrorTypeType) in ApiErrorType;
}

export async function createApiResponse<T>(response: Response): Promise<ApiResponse<T>> {
    const json = await response.json();
    if (response.ok) {
        return {
            ok: response.ok,
            status: response.status,
            data: json as T,
            error: null,
            problemDetail: null
        };
    }
    
    if (json && isApiErrorType(json.type)) {
        return {
            ok: false,
            status: response.status,
            data: null,
            error: json as ErrorResponse,
            problemDetail: null
        };
    }
    
    return {
        ok: false,
        status: response.status,
        data: null,
        error: null,
        problemDetail: {
            type: new URL(json.type),
            title: json.title,
            status: json.status,
            detail: json.detail,
            instance: json.instance ? new URL(json.instance) : null,
            properties: {
                ...json
            }
        }
    };
}