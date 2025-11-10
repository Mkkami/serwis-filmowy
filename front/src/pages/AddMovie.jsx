import { useReducer, useState } from "react";
import { movieInitialState, movieReducer } from "../reducers/MovieReducer";
import { formActions } from "../reducers/FormReducer";
import Categories from "../components/Categories";

function AddMovie() {
    const [state, dispatch] = useReducer(movieReducer, movieInitialState);

    const handleChange = (e) => {
        dispatch({
            type: formActions.changeField,
            field: e.target.name,
            value: e.target.value
        })
    }

    const handleCategoryUpdate = (newCategoriesSet) => {
        dispatch({
            type: formActions.changeField,
            field: 'categories',
            value: newCategoriesSet
        });
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        console.log("Data", {
            ...state,
        })

        alert("submitted");
    }

    return (
        <div className="movie-form form">
            <h2>Add new movie</h2>
            <form onSubmit={handleSubmit}>
                <div className="main-content">
                    <div className="form-container">
                        <div className="input-container">
                            <label htmlFor="title">Title</label>
                            <input placeholder="title" name="title" onChange={handleChange} type="text"></input>
                        </div>
                        <div className="input-container">
                            <label htmlFor="duration">Duration</label>
                            <input placeholder="minutes" name="duration" onChange={handleChange} type="number"></input>
                        </div>
                        <div className="input-container">
                            <label htmlFor="releaseYear">Release Year</label>
                            <input placeholder="year" name="releaseYear" onChange={handleChange} type="number"></input>
                        </div>
                    </div>
                    <div className="categories-wrapper">
                        <h3>Categories</h3>
                        <Categories selectedCategories={state.values.categories} setSelectedCategories={handleCategoryUpdate}/>
                    </div>
                </div>
                <button type="submit">Submit</button>
            </form>
        </div>
    )
}
export default AddMovie;