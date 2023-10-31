function copyToClipboard() {
  const url = document.querySelector("#short-url")
  navigator.clipboard.writeText(url.href)
  console.log(`copied "${url.href}" to clipbard`)
} 
