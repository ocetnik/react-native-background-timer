module.exports = {
  extends: ['airbnb', 'prettier'],
  plugins: ['import', 'jsx-a11y', 'prettier', 'react', 'react-hooks'],
  rules: {
    'class-methods-use-this': ['error', { exceptMethods: ['start', 'stop'] }],
    'prettier/prettier': ['error'],
  },
};
