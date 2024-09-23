export interface LoginResponse {
    message: string;
    data: {
        id : number;
        nombre: string;
    };
    is_success: boolean;
}
