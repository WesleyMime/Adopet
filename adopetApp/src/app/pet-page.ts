import { Page } from "./page";
import { Pet } from "./pet";

export interface PetsPage {
    content: Pet[],
    page: Page
}
