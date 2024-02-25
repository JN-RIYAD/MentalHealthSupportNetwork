// JavaScript for chatbot functionality
document.getElementById("sendMessageBtn").addEventListener("click", function() {
    var userInput = document.getElementById("userInput").value;
    sendMessage(userInput);
    document.getElementById("userInput").value = ""; // Clear input field after sending
});

function sendMessage(message) {
    var chatbox = document.getElementById("chatbox");
    var userMessage = '<div class="user-message">' + message + '</div>';
    chatbox.innerHTML += userMessage;
    chatbox.scrollTop = chatbox.scrollHeight; // Scroll to bottom of chatbox

    // Send message to backend AI model
    fetch('/api/chatbot?message=' + encodeURIComponent(message))
        .then(response => response.json())
        .then(data => {
            var botMessage = '<div class="bot-message">' + data.response + '</div>';
            chatbox.innerHTML += botMessage;
            chatbox.scrollTop = chatbox.scrollHeight; // Scroll to bottom of chatbox
        })
        .catch(error => {
            console.error('Error sending message to backend:', error);
            // Handle error (e.g., display error message in chat box)
        });
}
