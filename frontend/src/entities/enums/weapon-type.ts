import { z } from "zod";

export const WeaponTypeSchema = z.enum(["AXE", "PISTOL", "MACHINE_GUN"])

export type WeaponType = z.infer<typeof WeaponTypeSchema>

export const WeaponTypeTranslation = {
    AXE: "Топор",
    PISTOL: "Пистолет",
    MACHINE_GUN: "Пулемет"
} satisfies Record<WeaponType, string>