export class Pet {
    id: any;
    abrigo: any;
    name: any;
    age: any;
    size: any;
    adopted: any;
    address: any;
    image: any;
    description: any;

    constructor (
    id: string,
    abrigo: string | null,
    name: string,
    size: string,
    description: string,
    adopted: boolean,
    age: string,
    address: string,
    image: string
    ) {}
}