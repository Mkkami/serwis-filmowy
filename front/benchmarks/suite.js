import { Bench } from 'tinybench';

// ==========================================
// 1. Definicje 20 Kluczowych Metod Aplikacji
// ==========================================

// --- A. walidacja (Validation) ---

const validateEmail = (email) => {
  return String(email)
    .toLowerCase()
    .match(
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    );
};

const validatePasswordStrict = (password) => {
  // Min 8 chars, 1 uppercase, 1 lowercase, 1 number
  const regex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
  return regex.test(password);
};

const sanitizeInput = (input) => {
  return input.replace(/[&<>"']/g, function (m) {
    return {
      '&': '&amp;',
      '<': '&lt;',
      '>': '&gt;',
      '"': '&quot;',
      "'": '&#039;',
    }[m];
  });
};

const validateMovieTitle = (title) => {
  return (
    title.length > 0 && title.length < 100 && /^[a-zA-Z0-9\s:,-]+$/.test(title)
  );
};

// --- B. Logika Reducerów (State Management) ---

// Symulacja reducerów z plików src/reducers/*
const INITIAL_AUTH_STATE = { user: null, loading: false, error: false };
const loginReducer = (state, action) => {
  switch (action.type) {
    case 'LOGIN_START':
      return { user: null, loading: true, error: false };
    case 'LOGIN_SUCCESS':
      return { user: action.payload, loading: false, error: false };
    case 'LOGIN_FAILURE':
      return { user: null, loading: false, error: true };
    case 'LOGOUT':
      return { user: null, loading: false, error: false };
    default:
      return state;
  }
};

const moviesReducer = (state, action) => {
  switch (action.type) {
    case 'FETCH_START':
      return { ...state, isFetching: true };
    case 'FETCH_SUCCESS':
      return { ...state, movies: action.payload, isFetching: false };
    case 'FILTER_GENRE':
      return { ...state, filter: action.payload };
    default:
      return state;
  }
};

// --- C. Przetwarzanie Danych (Data Processing) ---

// 100 przykładowych filmów do testów sortowania/filtrowania
const sampleMovies = Array.from({ length: 100 }, (_, i) => ({
  id: i,
  title: `Movie ${i}`,
  year: 2000 + (i % 24),
  rating: (i % 10) + 1,
  genre: i % 2 === 0 ? 'Action' : 'Drama',
}));

const filterMoviesByGenre = (movies, genre) => {
  return movies.filter((movie) => movie.genre === genre);
};

const sortMoviesByYearDesc = (movies) => {
  return [...movies].sort((a, b) => b.year - a.year);
};

const searchMoviesLocal = (movies, query) => {
  const q = query.toLowerCase();
  return movies.filter((m) => m.title.toLowerCase().includes(q));
};

const mapApiDataToModel = (apiResponse) => {
  return apiResponse.map((item) => ({
    id: item._id || item.id,
    displayTitle: item.title.toUpperCase(),
    releaseYear: new Date(item.release_date || Date.now()).getFullYear(),
    isPopular: item.vote_average > 7.5,
  }));
};

const calculateAverageRating = (movies) => {
  if (!movies.length) return 0;
  const sum = movies.reduce((acc, curr) => acc + curr.rating, 0);
  return sum / movies.length;
};

// --- D. Utilities & Helpers ---

const jwtPayloadMock =
  'eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ';
const parseJwtSimple = (tokenPart) => {
  try {
    return JSON.parse(Buffer.from(tokenPart, 'base64').toString());
  } catch (_e) { // Zmieniono 'e' na '_e' aby zasygnalizować unused variable
    return null;
  }
};

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(amount);
};

const truncateDescription = (text, maxLength) => {
  if (text.length <= maxLength) return text;
  return text.substr(0, maxLength) + '...';
};

const generateSlug = (title) => {
  return title
    .toLowerCase()
    .trim()
    .replace(/[^\w\s-]/g, '')
    .replace(/[\s_-]+/g, '-')
    .replace(/^-+|-+$/g, '');
};

const checkTokenExpiration = (expTimestamp) => {
  return Date.now() >= expTimestamp * 1000;
};

const prepareHeaders = (token) => {
  return {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`,
  };
};

const transformSeriesData = (seriesList) => {
  // Complex transformation
  return seriesList.reduce((acc, series) => {
    acc[series.id] = {
      title: series.title,
      episodes: series.episodes ? series.episodes.length : 0,
    };
    return acc;
  }, {});
};

// ==========================================
// 2. Konfiguracja Benchmarków (Tinybench)
// ==========================================

const bench = new Bench({ time: 1000 }); // Uruchamiaj każdy test przez 1s

console.log('🚀 Start mikrobenchmarków (Tinybench)...');
console.log('⏳ Trwa analizowanie 20 kluczowych metod...\n');

// --- Dodawanie zadań do benchmarku ---

// 1-4: Validation
bench
  .add('1. validateEmail (valid)', () =>
    validateEmail('test.user@filmbase.com')
  )
  .add('2. validatePasswordStrict (complex)', () =>
    validatePasswordStrict('P@ssw0rd123!')
  )
  .add('3. sanitizeInput (XSS prevention)', () =>
    sanitizeInput('<script>alert("xss")</script>')
  )
  .add('4. validateMovieTitle (regex)', () =>
    validateMovieTitle('The Matrix Reloaded: Version 2.0')
  )

  // 5-8: Reducers
  .add('5. loginReducer (LOGIN_SUCCESS)', () =>
    loginReducer(INITIAL_AUTH_STATE, {
      type: 'LOGIN_SUCCESS',
      payload: { name: 'User' },
    })
  )
  .add('6. loginReducer (LOGIN_FAILURE)', () =>
    loginReducer(INITIAL_AUTH_STATE, { type: 'LOGIN_FAILURE' })
  )
  .add('7. moviesReducer (FETCH_SUCCESS)', () =>
    moviesReducer(
      { movies: [], isFetching: true },
      { type: 'FETCH_SUCCESS', payload: sampleMovies }
    )
  )
  .add('8. moviesReducer (FILTER updates)', () =>
    moviesReducer(
      { filter: 'All' },
      { type: 'FILTER_GENRE', payload: 'Action' }
    )
  )

  // 9-13: Data Processing (Arrays)
  .add('9. filterMoviesByGenre (100 items)', () =>
    filterMoviesByGenre(sampleMovies, 'Action')
  )
  .add('10. sortMoviesByYearDesc (100 items)', () =>
    sortMoviesByYearDesc(sampleMovies)
  )
  .add('11. searchMoviesLocal (text search)', () =>
    searchMoviesLocal(sampleMovies, 'Movie 5')
  )
  .add('12. mapApiDataToModel', () =>
    mapApiDataToModel(sampleMovies.slice(0, 50))
  ) //模拟 mapping
  .add('13. calculateAverageRating', () => calculateAverageRating(sampleMovies))

  // 14-20: Utilities
  .add('14. parseJwtSimple (Base64 decode)', () =>
    parseJwtSimple(jwtPayloadMock)
  )
  .add('15. formatCurrency (Intl)', () => formatCurrency(1234567.89))
  .add('16. truncateDescription (String ops)', () =>
    truncateDescription(
      'Really long movie description that needs to be cut off explicitly here.',
      20
    )
  )
  .add('17. generateSlug (Regex replacement)', () =>
    generateSlug('Star Wars: Episode IV - A New Hope')
  )
  .add('18. checkTokenExpiration (Date check)', () =>
    checkTokenExpiration(1893456000)
  )
  .add('19. prepareHeaders (Object creation)', () =>
    prepareHeaders('xyz-token-123')
  )
  .add('20. transformSeriesData (Reduce op)', () =>
    transformSeriesData(sampleMovies.slice(0, 20))
  ); // reusing sampleMovies structure

// ==========================================
// 3. Uruchomienie i Raport
// ==========================================

await bench.run();

console.table(bench.table());

// Zapisz wyniki do prostego JSON (opcjonalnie)
const results = bench.tasks.map((task) => ({
  'Task Name': task.name,
  'Average Time (ns)': (task.result.mean * 1000 * 1000).toFixed(2), // ms -> ns
  'Ops/Sec': Math.floor(task.result.hz),
  'Margin of Error': `±${task.result.rme.toFixed(2)}%`,
}));

console.log('\n📋 Interpretacja wyników:');
console.log(
  '- Average Time (ns): Średni czas wykonania w nanosekundach (niżej = lepiej)'
);
console.log('- Ops/Sec: Operacje na sekundę (wyżej = lepiej)\n');
