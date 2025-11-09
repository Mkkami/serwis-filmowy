import { formActions, formReducer } from "./FormReducer";

export const movieInitialState = {
    values: {
        title: "",
        duration: 0,
        releaseYear: 0,
        categories: []
    },
    errors: {
        title: "",
        duration: "",
        releaseYear: "",
        categories: []
    }
}

export function movieReducer(state, action) {
    if (action.type === formActions.reset) {
        return movieInitialState;
    }
    return formReducer(state, action);
}