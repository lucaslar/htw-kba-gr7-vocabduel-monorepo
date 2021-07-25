import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../model/internal/user';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class UserService {
    constructor(private readonly http: HttpClient) {}

    updateAccount$(user: User): Observable<User> {
        const url = `${environment.endpointUrl}/user/update-account`;
        return this.http.put<User>(url, user);
    }

    findUsers$(searchStr: string): Observable<User[]> {
        const url = `${environment.endpointUrl}/user/find`;
        return this.http.get<User[]>(url, { params: { searchStr } });
    }

    getUser$(
        value:
            | { username: string }
            | { email: string }
            | { id: string  }
    ): Observable<User> {
        const url = `${environment.endpointUrl}/user/get`;
        return this.http.get<User>(url, { params: value });
    }
}
