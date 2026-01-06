#!/bin/bash
cd "$(dirname "$0")"

echo "📦 Instalacja pakietu tinybench..."
npm install > /dev/null 2>&1

echo ""
echo "⚙️  Uruchamianie benchmarków..."
echo "--------------------------------------------------------"
npm start
echo "--------------------------------------------------------"
echo "✅ Benchmarki zakończone."
