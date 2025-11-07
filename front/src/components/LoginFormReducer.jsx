export const initialState = {
    values: {
        username: "",
        password: ""
    },
    errors: {
        username: "",
        password: "",
    }
};

export const formActions = {
    changeField: "CHANGE_FIELD",
    setError: "SET_ERROR",
    reset: "RESET"
};

export function loginFormReducer(state, action) {
    switch (action.type) {
        case formActions.changeField:
            return {
                ...state,
                values: {...state.values, [action.field]: action.value},
                errors: {...state.errors, [action.field]: ""}
            }
        case formActions.setError:
            return {
                ...state,
                errors: { ...state.errors, [action.field]: action.error },
            }
        case formActions.reset:
            return initialState;
        default:
            return state;
    }
}