import { useEffect, useRef, useState } from "react";
import { getCategories } from "../api/api";
import "../styles/Categories.css"

function Categories({selectedCategories, setSelectedCategories, heightLimit=false}) {
    const [isLoading, setIsLoading] = useState(true);
    const [categories, setCategories] = useState([]);
    const scrollRef = useRef(null);
    const [fadeTop, setFadeTop] = useState(false);
    const [fadeBottom, setFadeBottom] = useState(true);

    const updateScrollFade = () => {
        const el = scrollRef.current;
        if (!el) return;

        const atTop = el.scrollTop <= 0;
        const atBottom = el.scrollHeight - el.scrollTop <= el.clientHeight +1;
        setFadeTop(!atTop);
        setFadeBottom(!atBottom);
    };

    useEffect(() => {
        const fetchCategories = async () => {
            const res = await getCategories();
            const data = await res.json();
            setCategories(data);
            setIsLoading(false);
        }
        fetchCategories();
    }, []);

    const handleCategorySelect = (cat, isChecked) => {
        const newSet = new Set(selectedCategories);

        if (isChecked) {
            newSet.add(cat);
        } else {
            newSet.delete(cat);
        }

        setSelectedCategories(newSet);
    }

    return (
        <div className={`categories-container ${fadeTop && heightLimit ? "fade-top" : ""} ${fadeBottom && heightLimit ? "fade-bottom" : ""} `}>
            <div className={`categories ${heightLimit ? "height-limit" : ""}`} ref={scrollRef} onScroll={updateScrollFade}>
                {isLoading ? <p>Loading...</p>
            :
                categories.map((cat) =>
                    <label key={cat.id} htmlFor={`cat-${cat.id}`}>
                        <input id={`cat-${cat.id}`} 
                            type="checkbox" 
                            name={cat.name} 
                            onChange={(e) => handleCategorySelect(cat, e.target.checked)}
                            // checked={selectedCategories.has(cat.name)}
                            />
                        {cat.name}
                    </label>
                )}
            </div>
        </div>
    )
}
export default Categories;