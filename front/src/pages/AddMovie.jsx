import { useReducer, useState } from "react";
import { movieInitialState, movieReducer } from "../reducers/MovieReducer";
import { formActions } from "../reducers/FormReducer";
import Categories from "../components/Categories";
import '../styles/MovieForm.css'

function AddMovie() {
    const [categoriesOpen, setCategoriesOpen] = useState(false);
    const [state, dispatch] = useReducer(movieReducer, movieInitialState);

    const handleChange = (e) => {
        dispatch({
            type: formActions.changeField,
            field: e.target.name,
            value: e.target.value
        })
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        alert("submitted");
    }

    return (
        <div className="form">
            <form>
                <div className="form-container">
                    <div>
                        <label htmlFor="title">Title</label>
                        <input placeholder="title" name="title" onChange={handleChange} type="text"></input>
                    </div>
                    <div>
                        <label htmlFor="duration">Duration</label>
                        <input placeholder="minutes" name="duration" onChange={handleChange} type="number"></input>
                    </div>
                    <div>
                        <label htmlFor="releaseYear">Release Year</label>
                        <input placeholder="year" name="releaseYear" onChange={handleChange} type="number"></input>
                    </div>
                </div>
                <div>
                    <h3>Categories</h3>
                    <Categories/>
                </div>
                
            </form>
        </div>
    )
}
export default AddMovie;