import { useMutation, useQueryClient } from "@tanstack/react-query";
import { z } from "zod";
import { ApiInstance } from "./instance";
import axios from "axios";
import { clear } from "console";

export function setSession(session: {
  accessToken: string;
  refreshToken: string;
}) {
  localStorage.setItem("accessToken", session.accessToken);
  localStorage.setItem("refreshToken", session.refreshToken);
}

export function getAccessToken() {
  return localStorage.getItem("accessToken");
}

export function getRefreshToken() {
  return localStorage.getItem("refreshToken");
}

function clearSession() {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
}

ApiInstance.instance.interceptors.request.use(
  (config) => {
    const token = getAccessToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    } else {
      config.headers.Authorization = undefined;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

ApiInstance.instance.interceptors.response.use(
  (res) => {
    return res;
  },
  async (err) => {
    const originalConfig = err.config!;
    if (err.response) {
      // Access Token was expired
      if (err.response.status === 401 && !originalConfig._retry) {
        originalConfig._retry = true;
        try {
          const previousRefreshToken = getRefreshToken();
          if (!previousRefreshToken) {
            return Promise.reject(err);
          }
          const { accessToken, refreshToken } =
            await refreshAccessTokenFn(previousRefreshToken);
          setSession({ accessToken, refreshToken });

          return ApiInstance.instance(originalConfig);
        } catch (_error) {
          return Promise.reject(_error);
          clearSession();
        }
      }
    }

    return Promise.reject(err);
  },
);

export type SessionData = {
  refreshToken: string;
  accessToken: string;
};

const SignInResponseSchema = z.object({
  accessToken: z.string(),
  refreshToken: z.string(),
});
export const SignInSchema = z.object({
  username: z.string(),
  password: z.string(),
});
type SignIn = z.infer<typeof SignInSchema>;

export function useSignInMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (raw: SignIn) => {
      const values = SignInSchema.parse(raw);
      const { data } = await ApiInstance.api.login(values);
      return SignInResponseSchema.parse(data);
    },
    onSuccess: async (data) => {
      setSession({
        refreshToken: data.refreshToken,
        accessToken: data.accessToken,
      });
      queryClient.clear();
    },
  });
}

export const SignUpSchema = SignInSchema;
type SignUp = z.infer<typeof SignUpSchema>;

export function useSignUpMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (raw: SignUp) => {
      const values = SignUpSchema.parse(raw);
      const { data } = await ApiInstance.api.register(values);
      return SignInResponseSchema.parse(data);
    },
    onSuccess: async (data) => {
      setSession({
        refreshToken: data.refreshToken,
        accessToken: data.accessToken,
      });
      queryClient.clear();
    },
  });
}

export function useSignOutMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async () => {
      clearSession();
      queryClient.clear();
    },
  });
}

const RefreshTokenResponseSchema = SignInResponseSchema;
/**
 * Обновляет access token. Возвращает новые токены
 */
async function refreshAccessTokenFn(
  refreshToken: string,
): Promise<SessionData> {
  const { data: rawValues } = await axios.post<{ access: string }>(
    "/api/v1/refresh",
    refreshToken,
  );
  return RefreshTokenResponseSchema.parse(rawValues);
}
