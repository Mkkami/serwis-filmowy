export const formActions = {
    changeField: "CHANGE_FIELD",
    setError: "SET_ERROR",
    reset: "RESET"
};

export function formReducer(state, action) {
    switch(action.type) {
        case formActions.changeField:
            return {
                ...state,
                values: {...state.values, [action.field]: action.value},
                errors: {...state.errors, [action.field]: ""}
            }
        case formActions.setError:
            return {
                ...state,
                errors: {...state.errors, [action.field]: action.error}
            }
        case formActions.reset:
            if (action.initialState) {
                return action.initialState;
            }
            return state;
        default: return state;
    }
}